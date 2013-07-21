#!/usr/local/bin/thrift --gen java

namespace java edu.bupt.thrift.service
struct ServerMetrics{
  1: string serverip
  2: double metrics
}
service ZkTService {
    bool addCamera(1:string serverip,2:string camip);
    bool deleteCamera(1:string serverip,2:string camip);
    list<string> getAllCamsByServerIP(1:string serverip);
    bool deleteServerX(1:string serverip);
    double getMetrics(1:string serverip);
    map<string,double> getAllMetrics();
    ServerMetrics getBestMetricsEntry();
    map<string,double> getBestMetEntry();
    bool transferCam(1:string fromserver,2:string toserver,3:string camip);
}

