# ring-accept-param

Ring middleware that augments `:params` according to the specified `Accept` request-header field. Helps to indicate which type is acceptable for the response, for example `text/html`, `text/xml` or `application/json`. When no format can be deduced, a default value is set (`text/html`).

[![Build Status](https://secure.travis-ci.org/gmazelier/ring-accept-param.png)](https://secure.travis-ci.org/gmazelier/ring-accept-param.png)

## Usage

Augments `:params` with an entry identified by the key `accept` and the detected value. You can now render the responce in your routes given the desired format, a la Grails.

To be documented.

### Artifact

With Leiningen:

    [ring-accept-param "0.0.1"]

With Maven:

    <dependency>
      <groupId>ring-accept-param</groupId>
      <artifactId>ring-accept-param</artifactId>
      <version>0.0.1</version>
    </dependency>

### Configuration

To configure the middleware:

```clojure
(ns my.app
  (:use [ring.middleware.accept-param :only [wrap-accept-param]])
  (:require [compojure.handler :as handler]))

(defroutes main-routes
  ...)

(def app
  (-> (handler/api main-routes)
      (wrap-accept-param)))
```

## See also

You can also try:

+ https://github.com/ngrunwald/ring-middleware-format

## License

Copyright (C) 2012 Gaylord Mazelier

Distributed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html), the same as Clojure.
