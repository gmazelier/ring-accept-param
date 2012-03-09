(ns ring.middleware.accept-param-tests
	(:use clojure.test)
	(:require [ring.middleware.accept-param :as mdw]))

(deftest is-json
	(is (mdw/accept-format? "application/json" "^application/json")))

(deftest xml-is-not-json
	(is (not (mdw/accept-format? "text/xml" "^application/json"))))

(deftest empty-string-is-not-json
	(is (not (mdw/accept-format? "" "^application/json"))))

(deftest nil-is-not-json
	(is (not (mdw/accept-format? nil "^application/json"))))
