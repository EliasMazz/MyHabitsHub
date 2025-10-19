import Foundation
import ComposeApp
import FirebaseAnalytics
import FirebaseCore

class FirebaseAnalyticsImpl: ComposeApp.Analytics {
    
    func logEvent(event: String, params: [String : Any]?) {
        var eventParams: [String: Any] = [:]
        params?.forEach { key, value in eventParams[key] = "\(value)" }
        Analytics.logEvent(event, parameters: eventParams)
    }
    
    func setEnabled(enabled: Bool) {
        Analytics.setAnalyticsCollectionEnabled(enabled)
    }
    
}
