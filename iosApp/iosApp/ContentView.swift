import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(
            mapUIViewController: { (isPortrait: KotlinBoolean, shouldMoveCamera: KotlinBoolean, selectedTrain: Train?, selectedStation: Station?, routeLine: [ComposeApp.Shape]) -> UIViewController in
                return UIHostingController(rootView: GoogleMapView(isPortrait: isPortrait, shouldMoveCamera: shouldMoveCamera, selectedTrain: selectedTrain, selectedStation: selectedStation, routeLine: routeLine))
            }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}



