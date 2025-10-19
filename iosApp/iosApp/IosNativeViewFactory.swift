import Foundation
import ComposeApp
import SwiftUI
import UIKit

class IosNativeViewFactory: NativeViewFactory{
    static var shared = IosNativeViewFactory()
    
    func createSwiftTextView(text: String) -> UIViewController {
        let swiftUIView = Text(text)
        return UIHostingController(rootView: swiftUIView)
    }
}
