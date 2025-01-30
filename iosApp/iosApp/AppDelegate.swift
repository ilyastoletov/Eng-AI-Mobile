//
//  AppDelegate.swift
//  iosApp
//
//  Created by Ilya Stoletov on 30.01.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import FirebaseCore
import ComposeApp
import NSExceptionKtCrashlytics
import AppMetricaCore

class AppDelegate: NSObject, UIApplicationDelegate {
    
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
    ) -> Bool {
        let secrets = AppSecrets.init()
        let appmetricaConfig = AppMetricaConfiguration(apiKey: secrets.appmetricaKey)
        AppMetrica.activate(with: appmetricaConfig!)
        FirebaseApp.configure()
        NSExceptionKt.addReporter(.crashlytics(causedByStrategy: .append))
        return true
    }
    
}
