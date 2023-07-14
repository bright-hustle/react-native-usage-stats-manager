/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable react-hooks/exhaustive-deps */
import * as React from 'react';

import { StyleSheet, Text, View, processColor } from 'react-native';
import {
  EventFrequency,
  checkForPermission,
  queryAndAggregateUsageStats,
  queryEvents,
  queryEventsStats,
  queryUsageStats,
  showUsageAccessSettings,
} from 'react-native-usage-stats-manager';
import moment from 'moment';
import { COLORS } from './colors';
import { PieChart } from 'react-native-charts-wrapper';

export default function App() {
  // const [result, setRes ult] = React.useState<any | undefined>('test');
  const startToday = moment().startOf('day');

  const startDateString = startToday.format('YYYY-MM-DD HH:mm:ss');
  const endDateString = new Date();

  const startMilliseconds = new Date(startDateString).getTime();
  const endMilliseconds = new Date(endDateString).getTime();

  const [dataView, setDataView]: any = React.useState([]);
  const [dataNew, setDataNew]: any = React.useState({
    data: {
      dataSets: [
        {
          values: [],
          // label: 'Pie dataset',
          config: {
            colors: [
              processColor(COLORS.BUTTONCOLOR1),
              processColor(COLORS.ORANGE_COLOR),
              processColor(COLORS.GREEN_COLOR),
              processColor(COLORS.RED_COLOR),
              processColor(COLORS.TEAL_COLOR),
            ],
            valueTextSize: 20,
            valueTextColor: processColor('green'),
            sliceSpace: 2,
            selectionShift: 10,
            // xValuePosition: "OUTSIDE_SLICE",
            // yValuePosition: "OUTSIDE_SLICE",
            valueFormatter: "#'%'",
            valueLineColor: processColor('green'),
            valueLinePart1Length: 0.5,
          },
        },
      ],
    },
  });
  const [dataNewPie, setDataNewPie]: any = React.useState({
    legend: {
      enabled: false,
      // textSize: 15,
      // form: 'CIRCLE',
      // horizontalAlignment: 'RIGHT',
      // verticalAlignment: 'CENTER',
      // orientation: 'VERTICAL',
      // wordWrapEnabled: true,
    },
    highlights: [{ x: 2 }],
    description: {
      text: '',
      textSize: 15,
      textColor: processColor('darkgray'),
    },
  });

  const getHours = (seconds: any) => {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);

    return `${hours} hr : ${minutes} min`;
  };

  const handleSelect = (event: any) => {
    // let entry = event.nativeEvent;
    // if (entry == null) {
    //   this.setState({...this.state, selectedEntry: null});
    // } else {
    //   this.setState({...this.state, selectedEntry: JSON.stringify(entry)});
    // }

    console.log(event.nativeEvent);
  };
  React.useEffect(() => {
    checkForPermission().then((res: any) => {
      console.log('permission ::', res);
      if (!res) {
        showUsageAccessSettings('');
      }
    });
    // queryEvents(startMilliseconds, endMilliseconds).then((da: any) => {
    //   console.log('query events ::', da);
    // });
    queryEventsStats(
      EventFrequency.INTERVAL_BEST,
      startMilliseconds,
      endMilliseconds
    ).then((da: any) => {
      console.log('query events ::', da);
    });
    queryAndAggregateUsageStats(startMilliseconds, endMilliseconds).then(
      (res: any) => {
        console.log(res);
        const value: any = Object.entries(res).sort(
          ([, a]: any, [, b]: any) =>
            b.totalTimeInForeground - a.totalTimeInForeground
        );
        let dataTempArr: any = [];

        for (let i = 0; i < 4; i++) {
          const dataTemp = {
            value: value[i][1].totalTimeInForeground,
            label: value[i][1].appName
              ? value[i][1].appName
              : value[i][1].packageName,
          };
          dataTempArr.push(dataTemp);
        }
        const dataArr = dataTempArr.reduce(
          (a: any, b: any) => a + b.population,
          0
        );
        let dataTempPie: any = {
          data: {
            dataSets: [
              {
                values: dataTempArr,
                config: {
                  colors: [
                    processColor(COLORS.BUTTONCOLOR1),
                    processColor(COLORS.ORANGE_COLOR),
                    processColor(COLORS.GREEN_COLOR),
                    processColor(COLORS.RED_COLOR),
                    processColor(COLORS.TEAL_COLOR),
                  ],
                  valueTextSize: 15,
                  valueTextColor: processColor('white'),
                  sliceSpace: 5,
                  selectionShift: 13,
                  // xValuePositio: "OUTSIDE_SLICE",
                  // yValuePosition: "OUTSIDE_SLICE",
                  valueFormatter: "#'%'",
                  valueLineColor: processColor('green'),
                  valueLinePart1Length: 0.5,
                },
              },
            ],
          },
        };
        setDataNew(dataTempPie);
        setDataView(dataTempArr);
      }
    );
  }, []);

  return (
    <View>
      <PieChart
        style={styles.chart}
        logEnabled={true}
        // chartBackgroundColor={processColor('green')}
        chartDescription={dataNewPie.description}
        data={dataNew.data}
        legend={dataNewPie.legend}
        highlights={dataNewPie.highlights}
        // extraOffsets={{ left: 10, top: 5, right: 10, bottom: 2 }}
        // entryLabelColor={processColor('green')}
        entryLabelFontFamily={'HelveticaNeue-Medium'}
        drawEntryLabels={true}
        rotationEnabled={true}
        rotationAngle={45}
        usePercentValues={true}
        styledCenterText={{
          text: '',
          color: processColor('pink'),
          fontFamily: 'HelveticaNeue-Medium',
          size: 20,
        }}
        centerTextRadiusPercent={100}
        holeRadius={25}
        holeColor={processColor('#f0f0f0')}
        transparentCircleRadius={30}
        transparentCircleColor={processColor('#f0f0f088')}
        maxAngle={360}
        onSelect={handleSelect}
        onChange={(event: any) => console.log(event.nativeEvent)}
      />
      {dataView.map((data: any) => {
        return (
          <View
            style={{ alignItems: 'center', justifyContent: 'center' }}
            key={data.label}
          >
            <Text>{data.label}</Text>
            <Text>{getHours(data.value)}</Text>
          </View>
        );
      })}
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
  chart: {
    // flex: 1,
    height: 350,
  },
});
