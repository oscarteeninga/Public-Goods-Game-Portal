import { Component, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  encapsulation: ViewEncapsulation.None  // set to allow background color of the project from app.component.css settings
})
export class AppComponent {
  title = 'SInG';
}
