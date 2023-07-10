
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNUsageStatsManagerSpec.h"

@interface UsageStatsManager : NSObject <NativeUsageStatsManagerSpec>
#else
#import <React/RCTBridgeModule.h>

@interface UsageStatsManager : NSObject <RCTBridgeModule>
#endif

@end
