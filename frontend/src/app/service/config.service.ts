import {Injectable} from '@angular/core';
import {Configuration} from "../components/model/Configuration";
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ChartData} from "chart.js";

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  configUrl = "url"

  constructor(private http: HttpClient) {
  }

  getSimulationChartData(config: Configuration): Observable<ChartData[]> {
    return this.http.post<ChartData[]>(this.configUrl, {config: config})
  }
}
