//
//  GoogleMapView.swift
//  iosApp
//
//  Created by Jason Bromfield on 2025-05-11.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import GoogleMaps
import ComposeApp

struct GoogleMapView: UIViewRepresentable {
    var isPortrait: KotlinBoolean = true
    var shouldMoveCamera: KotlinBoolean = false
    var selectedTrain: Train?
    var selectedStation: Station?
    var routeLine: [ComposeApp.Shape] = []
    func makeUIView(context: Context) -> GMSMapView {
        let lat = 43.682636
        let lon = -79.353059
        let options = GMSMapViewOptions()
        options.camera = GMSCameraPosition.camera(withLatitude: lat, longitude: lon, zoom: 10.0)

        let mapView = GMSMapView(options: options)
        let darkModeJson = NSURL(fileURLWithPath: Bundle.main.path(forResource: "dark_mode", ofType: "json")!) as URL
        do {
            mapView.mapStyle = try GMSMapStyle(contentsOfFileURL: darkModeJson)
        } catch {
            print("Unable to load map style.")
        }

        if (selectedTrain?.location != nil) {
            let marker = GMSMarker()
            marker.position = CLLocationCoordinate2D(latitude: (selectedTrain?.location!.lat)!, longitude: (self.selectedTrain?.location!.lon)!)
            marker.title = selectedTrain?.name
            if (selectedTrain?.departed == false) {
                marker.snippet = "Departed"
            } else if (selectedTrain?.arrived == true) {
                marker.snippet = "Arrived"
            } else {
                marker.snippet = "Next stop: \(selectedTrain?.nextStop?.name ?? "") in \(selectedTrain?.nextStop?.eta ?? "")"
            }
            marker.map = mapView
        }
        if (selectedStation != nil) {
            let marker = GMSMarker()
            marker.position = CLLocationCoordinate2D(latitude: (selectedTrain?.location!.lat)!, longitude: (self.selectedTrain?.location!.lon)!)
            marker.title = selectedStation?.name
            marker.map = mapView
        }
        let path = GMSMutablePath()
        routeLine.forEach { shape in
            path.add(CLLocationCoordinate2D(latitude: shape.lat, longitude: shape.lon))
        }
        let line = GMSPolyline(path: path)
        line.map = mapView
        
        return mapView
    }

    func updateUIView(_ uiView: GMSMapView, context: Context) {}
}
