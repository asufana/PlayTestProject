

# DDD Module For PlayFramework

### Env

* Play! Framework 1.2.5

### Support

* Abstract Entity Class
* Abstract ValueObject Class

### Install

    $ play new SampleProject
    $ vim SampleProject/conf/dependencies.yml

    require:
        - play
        - asufana -> PlayDDD [0.1,)

    repositories:
        - asufana_playmodules_github:
            type:       http
            artifact:   "https://github.com/[organisation]/PlayDDD/raw/master/dist/[module]-[revision].zip"
            contains:
                - asufana -> *

    $ play dependencies SampleProject --clearcache
    $ play eclipsify SampleProject


---

*Makoto Hanafusa ( <a href="https://twitter.com/#!/asufana" target="_blank">@asufana</a> )*


