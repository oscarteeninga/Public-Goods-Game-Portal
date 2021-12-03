import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameConnectionComponent } from './game-connection.component';

describe('GameConnectionComponent', () => {
  let component: GameConnectionComponent;
  let fixture: ComponentFixture<GameConnectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GameConnectionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GameConnectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
