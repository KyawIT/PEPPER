export interface ITagalongStory {
  id: number;
  name: string;
  storyIcon: string;
  isEnabled: string;
}

export interface IStep {
  id: number;
  duration: number;
  moveNameAndDuration: string;
  text: string;
  image: string;
  index: number;
}
