import { Component, ElementRef, inject, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RouterOutlet, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { STORY_URL } from '../app.config';
import { IStep, ITagalongStory } from '../../models/tagalongstories.model';
import { CommonModule } from '@angular/common';
import { forkJoin } from 'rxjs';
import { FormsModule } from '@angular/forms';
import e from 'express';

@Component({
  selector: 'app-createstory',
  standalone: true,
  imports: [RouterOutlet, RouterModule, CommonModule, FormsModule],
  templateUrl: './createstory.component.html',
  styleUrl: './createstory.component.css'
})
export class CreatestoryComponent {
  private baseUrl = inject(STORY_URL);
  private http = inject(HttpClient);
  public duration = [5, 10, 15]
  public moves = ["emote_hurra", "essen", "gehen", "hand_heben", "highfive_links", "highfive_rechts", "klatschen", "strecken", "umher_sehen", "winken"]
  public moveNames = ["Hurra", "Essen", "Gehen", "Hand heben", "Highfive links", "Highfive rechts", "Klatschen", "Strecken", "Umher sehen", "Winken"]

  @ViewChild('tbody') tbody!: ElementRef;

  public tagalongstory: ITagalongStory = {
    id: 0,
    name: "string",
    storyIcon: "string",
    isEnabled: "string"
  };

  public steps: IStep[] = [];

  private id: number = 0;

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.id = params['id'];
    });

    if (this.id) {

      this.http.get<ITagalongStory>(this.baseUrl + "/" + this.id).subscribe(story => {
        this.tagalongstory = story;
      });

      this.http.get<IStep[]>(this.baseUrl + "/" + this.id + "/steps").subscribe(s => {
        this.steps = s;
      });
    }
    console.log(this.tagalongstory);
  }

  public addStep() {
    this.steps.push({
      id: 0,
      duration: 0,
      moveNameAndDuration: '',
      text: '',
      image: 'https://fakeimg.pl/600x400?text=Bild+Hochladen',
      index: this.steps.length+1
    });
  }



}
