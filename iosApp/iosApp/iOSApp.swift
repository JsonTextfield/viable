import SwiftUI
import ComposeApp
import GoogleMaps

@main
struct iOSApp: App {
    init() {
        GMSServices.provideAPIKey(apiKey)
        KoinKt.doInitKoin()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
