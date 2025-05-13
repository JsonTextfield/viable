import ComposeApp
import GoogleMaps
//
//  GoogleMapView.swift
//  iosApp
//
//  Created by Jason Bromfield on 2025-05-11.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI

struct GoogleMapView: UIViewRepresentable {
    var isPortrait: KotlinBoolean = true
    var shouldMoveCamera: KotlinBoolean = false
    var selectedTrain: Train?
    var selectedStation: Station?
    var routeLine: [ComposeApp.Shape] = []
    var lineColour: [KotlinFloat] = []
    func makeUIView(context: Context) -> GMSMapView {
        let options = GMSMapViewOptions()
        options.camera = GMSCameraPosition.camera(
            withLatitude: 43.682636,
            longitude: -79.353059,
            zoom: 8.0
        )

        let mapView = GMSMapView(options: options)
        applyInitialMapStyle(mapView)
        updateMapContent(mapView)
        mapView.padding = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        return mapView
    }

    private func applyInitialMapStyle(_ mapView: GMSMapView) {

        let isDarkMode = UITraitCollection.current.userInterfaceStyle == .dark
        if isDarkMode {
            let darkModeJson =
                NSURL(
                    fileURLWithPath: Bundle.main.path(
                        forResource: "dark_mode",
                        ofType: "json"
                    )!
                ) as URL
            do {
                mapView.mapStyle = try GMSMapStyle(
                    contentsOfFileURL: darkModeJson
                )
            } catch {
                print("Unable to load map style.")
            }
        }
    }
    private func updateMapContent(_ mapView: GMSMapView) {
        mapView.clear()

        // Update train marker
        if let train = selectedTrain, let location = train.location {
            let marker = GMSMarker()
            marker.position = CLLocationCoordinate2D(
                latitude: location.lat,
                longitude: location.lon
            )
            marker.title = train.name
            marker.snippet =
                train.departed == false
                ? "Departed"
                : (train.arrived == true
                    ? "Arrived"
                    : "Next stop: \(train.nextStop?.name ?? "") in \(train.nextStop?.eta ?? "")")
            marker.map = mapView
            mapView.animate(to: GMSCameraPosition.camera(withLatitude: train.location!.lat, longitude: train.location!.lon, zoom: mapView.camera.zoom))
        }

        // Update station marker
        if let station = selectedStation {
            let marker = GMSMarker()
            marker.position = CLLocationCoordinate2D(
                latitude: station.lat,
                longitude: station.lon
            )
            marker.title = station.name
            marker.map = mapView
        }

        // Update route line
        let path = GMSMutablePath()
        routeLine.forEach { shape in
            path.add(
                CLLocationCoordinate2D(
                    latitude: shape.lat,
                    longitude: shape.lon
                )
            )
        }
        let line = GMSPolyline(path: path)
        line.strokeWidth = 3
        line.strokeColor = UIColor(red: CGFloat(truncating: lineColour[0]), green: CGFloat(truncating: lineColour[1]), blue: CGFloat(truncating: lineColour[2]), alpha: CGFloat(truncating: lineColour[3]))
        line.map = mapView

        // Optionally move the camera
        if shouldMoveCamera == true, let train = selectedTrain,
            let location = train.location
        {
            let cameraUpdate = GMSCameraUpdate.setTarget(
                CLLocationCoordinate2D(
                    latitude: location.lat,
                    longitude: location.lon
                )
            )
            mapView.animate(with: cameraUpdate)
        }
    }
    func updateUIView(_ uiView: GMSMapView, context: Context) {
        print("updating from updateUIView")
        updateMapContent(uiView)
    }
}
