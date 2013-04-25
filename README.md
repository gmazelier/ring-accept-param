# ring-accept-param

Ring middleware that augments `:params` according to the specified `Accept` request-header field. Helps to indicate which type is acceptable for the response, for example `text/html`, `text/xml` or `application/json`. When no format can be deduced, a default value is set (`text/html`).

[![Build Status](https://secure.travis-ci.org/gmazelier/ring-accept-param.png)](https://secure.travis-ci.org/gmazelier/ring-accept-param.png)

## Usage

Augments `:params` with an entry identified by the key `:accept` and the detected value. You can now render the response in your routes given the desired format, a la Grails.

### Artifact

With Leiningen:

    [ring-accept-param "0.1.1"]

With Maven:

    <dependency>
      <groupId>ring-accept-param</groupId>
      <artifactId>ring-accept-param</artifactId>
      <version>0.1.1</version>
    </dependency>

### Configuration

To configure and use this middleware:

```clojure
(ns my-app.handler
  (:use [compojure.core :only [defroutes GET]]
        [ring.middleware.accept-param :only [wrap-accept-param]]
        [cheshire.core :only [generate-string]])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defn do-json-hello []
  {:status 200
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body (generate-string {:response "Hello World"})})

(defroutes app-routes
  (GET "/" {params :params}
    (do (println params)
    (case (:accept params)
      "json" (do-json-hello)
      "html" "Hello World")))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
    wrap-accept-param
    (handler/site)))
```

You can test this with `curl`:
```
$ curl -i http://127.0.01:3000
HTTP/1.1 200 OK
Date: Wed, 22 Aug 2012 20:49:14 GMT
Content-Type: text/html;charset=UTF-8
Content-Length: 11
Server: Jetty(7.6.1.v20120215)

Hello World
```

And with a specified `Accept: application/json` header:
```
$ curl -i http://127.0.01:3000 -H "Accept: application/json"
HTTP/1.1 200 OK
Date: Wed, 22 Aug 2012 20:49:18 GMT
Content-Type: application/json;charset=utf-8
Content-Length: 26
Server: Jetty(7.6.1.v20120215)

{"response":"Hello World"}
```

## Roadmap

Bunch of planned features:

+ Add more types like plain text or YAML.
+ Consider vendor-specific types in a non-strict mode.
+ Allow middleware configuration (default type, strict mode, etc.). 
+ Implement Accept request-header field [specs](http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html).

## See also

You can also try:

+ https://github.com/ngrunwald/ring-middleware-format

## License

Copyright (C) 2012 Gaylord Mazelier

Distributed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html), the same as Clojure.
