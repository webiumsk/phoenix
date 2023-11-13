import Foundation
import Combine
import PhoenixShared
import os.log

#if DEBUG && true
fileprivate var log = Logger(
	subsystem: Bundle.main.bundleIdentifier!,
	category: "KotlinPublishers+Lightning"
)
#else
fileprivate var log = Logger(OSLog.disabled)
#endif


extension Lightning_kmpElectrumClient {
	
	func notificationsSequence() -> AnyAsyncSequence<Lightning_kmpElectrumSubscriptionResponse> {
		
		return self.notifications
			.compactMap { $0 }
			.eraseToAnyAsyncSequence()
	}
}

// MARK: -
extension Lightning_kmpElectrumWatcher {
	
	func upToDateSequence() -> AnyAsyncSequence<Int64> {
		
		return self.openUpToDateFlow()
			.compactMap { $0 }
			.map { $0.int64Value }
			.eraseToAnyAsyncSequence()
	}
}

// MARK: -
extension Lightning_kmpNodeParams {
	
	func nodeEventsSequence() -> AnyAsyncSequence<Lightning_kmpNodeEvents> {
		
		return self.nodeEvents
			.compactMap { $0 }
			.eraseToAnyAsyncSequence()
	}
}
