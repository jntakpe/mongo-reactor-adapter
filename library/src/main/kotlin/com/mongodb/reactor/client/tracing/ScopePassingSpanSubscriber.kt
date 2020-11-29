package com.mongodb.reactor.client.tracing

import brave.propagation.CurrentTraceContext
import brave.propagation.TraceContext
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import reactor.core.CoreSubscriber
import reactor.core.Fuseable
import reactor.core.Scannable
import reactor.util.context.Context

/**
 * Trace representation of the subscriber, inspired from Spring Sleuth
 * @param subscriber regular subscriber
 * @param ctx Reactor's context
 * @param currentTraceContext OpenTracing's current trace context
 * @param parent OpenTracing's parent context
 */
internal class ScopePassingSpanSubscriber<T>(
    private val subscriber: Subscriber<in T>,
    ctx: Context,
    private val currentTraceContext: CurrentTraceContext,
    private val parent: TraceContext,
) : Scannable, Subscription, CoreSubscriber<T>, Fuseable.QueueSubscription<T> {

    private var subscription: Subscription? = null
    private val context = ctx.getOrEmpty<Any>(TraceContext::class)
        .filter { parent != it }
        .map { ctx.put(TraceContext::class, parent) }
        .orElse(ctx)

    override fun scanUnsafe(key: Scannable.Attr<Any>): Any? {
        return if (key === Scannable.Attr.PARENT) subscription else subscriber.takeIf { key === Scannable.Attr.ACTUAL }
    }

    override fun request(count: Long) {
        currentTraceContext.maybeScope(parent).use { subscription?.request(count) }
    }

    override fun cancel() {
        currentTraceContext.maybeScope(parent).use { subscription?.cancel() }
    }

    override fun onSubscribe(subscription: Subscription) {
        this.subscription = subscription
        currentTraceContext.maybeScope(parent).use { subscriber.onSubscribe(this) }
    }

    override fun onNext(item: T) {
        currentTraceContext.maybeScope(parent).use { subscriber.onNext(item) }
    }

    override fun onError(throwable: Throwable) {
        currentTraceContext.maybeScope(parent).use { subscriber.onError(throwable) }
    }

    override fun onComplete() {
        currentTraceContext.maybeScope(parent).use { subscriber.onComplete() }
    }

    override val size: Int
        get() = 0

    override fun isEmpty(): Boolean = true

    override fun clear() {
        // No op
    }

    override fun poll(): T? = null

    override fun requestFusion(p0: Int): Int = Fuseable.NONE

    override fun currentContext(): Context = context
}
