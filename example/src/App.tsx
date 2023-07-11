/* eslint-disable react-hooks/exhaustive-deps */
import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import {
  EventFrequency,
  checkForPermission,
  queryUsageStats,
  showUsageAccessSettings,
} from 'react-native-usage-stats-manager';

export default function App() {
  // const [result, setRes ult] = React.useState<any | undefined>('test');
  const startDateString = '2023-06-11T12:34:56';
  const endDateString = '2023-07-11T12:34:56';

  const startMilliseconds = new Date(startDateString).getTime();
  const endMilliseconds = new Date(endDateString).getTime();
  React.useEffect(() => {
    checkForPermission().then((res: any) => {
      console.log('permission ::', res);
      if (!res) {
        showUsageAccessSettings('');
      }
    });
    queryUsageStats(
      EventFrequency.INTERVAL_DAILY,
      startMilliseconds,
      endMilliseconds
    ).then((res: any) => {
      console.log(res);
    });
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
