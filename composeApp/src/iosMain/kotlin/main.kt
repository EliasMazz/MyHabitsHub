import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.yolo.myhabitshub.root.App
import com.yolo.myhabitshub.util.LocalNativeViewFactory
import com.yolo.myhabitshub.util.NativeViewFactory
import com.yolo.myhabitshub.util.SwiftLibDependencyFactory
import com.yolo.myhabitshub.util.swiftLibDependenciesModule
import org.koin.core.KoinApplication
import platform.UIKit.UIViewController

fun MainViewController(nativeViewFactory: NativeViewFactory): UIViewController =
    ComposeUIViewController {
        CompositionLocalProvider(LocalNativeViewFactory provides nativeViewFactory) {
            App()
        }
    }

//This is called on application started on Swift side
fun KoinApplication.provideSwiftLibDependencyFactory(factory: SwiftLibDependencyFactory) =
    run { modules(swiftLibDependenciesModule(factory)) }