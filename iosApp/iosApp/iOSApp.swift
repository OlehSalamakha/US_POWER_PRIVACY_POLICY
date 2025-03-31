import SwiftUI
import FirebaseCore
import FirebaseMessaging
import ComposeApp

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()
        AppInitializer.shared.onApplicationStart()
        
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(appDidBecomeActive),
            name: UIApplication.didBecomeActiveNotification,
            object: nil
        )
        
        return true
    }
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
    }
    
    
    @objc func appDidBecomeActive() {
        print("100500 appDidBecomeActive")
        UNUserNotificationCenter.current().removeAllDeliveredNotifications()
        UIApplication.shared.applicationIconBadgeNumber = 0
    }
    
}

@main
struct iOSApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    var body: some Scene {
        WindowGroup {
            ContentView()
                .background(Color(UIColor(red: 0/255, green: 0/255, blue: 105/255, alpha: 1.0)).ignoresSafeArea(edges: .all))
            //                .ignoresSafeArea(edges: .bottom) // q Add this line
                .ignoresSafeArea(.keyboard)
        }
    }
}


