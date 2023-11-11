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
	
	fileprivate struct _Key {
		static var notificationsPublisher = 0
	}
	
	func notificationsPublisher() -> AnyPublisher<Lightning_kmpElectrumSubscriptionResponse, Never> {
		
		self.getSetAssociatedObject(storageKey: &_Key.notificationsPublisher) {
			
			/// Transforming from Kotlin:
			/// `notifications: Flow<ElectrumSubscriptionResponse>`
			///
			KotlinPassthroughSubject<AnyObject>(
				self.notifications._bridgeToObjectiveC()
			)
			.compactMap { $0 as? Lightning_kmpElectrumSubscriptionResponse }
			.eraseToAnyPublisher()
		}
	}
}

// MARK: -
extension Lightning_kmpElectrumWatcher {
	
	fileprivate struct _Key {
		static var upToDatePublisher = 0
	}
	
	func upToDatePublisher() -> AnyPublisher<Int64, Never> {
		
		self.getSetAssociatedObject(storageKey: &_Key.upToDatePublisher) {
			
			/// Transforming from Kotlin:
			/// `openUpToDateFlow(): Flow<Long>`
			///
			KotlinPassthroughSubject<AnyObject>(
				self.openUpToDateFlow()._bridgeToObjectiveC()
			)
			.compactMap { $0 as? KotlinLong }
			.map { $0.int64Value }
			.eraseToAnyPublisher()
		}
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
