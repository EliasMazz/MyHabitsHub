import Foundation
import ComposeApp
import SwiftUI
import UIKit

class SwiftLibDependencyFactoryImpl: SwiftLibDependencyFactory {

    static var shared = SwiftLibDependencyFactoryImpl()

    func provideFeatureFlagManagerImpl() -> FeatureFlagManager {
        return FeatureFlagManagerImpl()
    }

    func provideFirebaseAnalyticsImpl() -> any Analytics {
        return FirebaseAnalyticsImpl()
    }

}
