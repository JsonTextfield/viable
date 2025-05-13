import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(
            mapUIViewController: { (isPortrait: KotlinBoolean, shouldMoveCamera: KotlinBoolean, selectedTrain: Train?, selectedStation: Station?, routeLine: [ComposeApp.Shape], lineColour: [KotlinFloat]) -> UIViewController in
                let gmapView = GoogleMapView(isPortrait: isPortrait, shouldMoveCamera: shouldMoveCamera, selectedTrain: selectedTrain, selectedStation: selectedStation, routeLine: routeLine, lineColour: lineColour)
                return UIHostingController(rootView: gmapView)
            }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        print("updating from updateUIViewController")
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea()
    }
}



