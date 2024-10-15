import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-createstory',
  standalone: true,
  imports: [],
  templateUrl: './createstory.component.html',
  styleUrl: './createstory.component.css'
})
export class CreatestoryComponent {

  private id: number = 0;

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.id = params['id'];
      console.log(this.id);
    });
  }

}
