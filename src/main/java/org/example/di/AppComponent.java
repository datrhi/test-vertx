package org.example.di;

import dagger.Component;
import javax.inject.Singleton;
import org.example.verticles.NoteVerticle;


@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
  void inject(NoteVerticle noteVerticle);
}
