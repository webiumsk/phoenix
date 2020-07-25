package fr.acinq.phoenix.app.ctrl

import fr.acinq.phoenix.ctrl.MVI
import fr.acinq.phoenix.io.AppMainScope
import fr.acinq.phoenix.utils.newLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import org.kodein.di.DIAware


@OptIn(ExperimentalCoroutinesApi::class)
abstract class AppController<M : MVI.Model, I : MVI.Intent> : MVI.Controller<M, I>(), DIAware, CoroutineScope {

    override val coroutineContext = AppMainScope().coroutineContext + Job()

    protected val logger by lazy { newLogger() }

    private val models = ConflatedBroadcastChannel<M>()

    final override fun subscribe(onModel: (M) -> Unit): () -> Unit {
        val job = launch {
            models.openSubscription().consumeEach { onModel(it) }
        }

        return ({ job.cancel() })
    }

    protected fun model(model: M) {
        logger.info { "Model: $model" }
        launch { models.send(model) }
    }

    protected abstract fun process(intent: I)

    final override fun intent(intent: I) {
        logger.info { "Intent: $intent" }
        process(intent)
    }

}