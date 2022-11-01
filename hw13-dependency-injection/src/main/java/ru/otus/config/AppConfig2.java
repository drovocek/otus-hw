package ru.otus.config;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@AppComponentsContainerConfig(order = 1)
public class AppConfig2 {

    @AppComponent(order = 0, name = "equationPreparer2")
    public String  equationPreparer(){
        return "equationPreparer2";
    }

}
