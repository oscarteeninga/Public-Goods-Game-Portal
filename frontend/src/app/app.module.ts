import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { ChartComponent } from './components/chart/chart.component';
import { ConfigComponent } from './components/config/config.component';

@NgModule({
  declarations: [
    AppComponent,
    ChartComponent,
    ConfigComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
