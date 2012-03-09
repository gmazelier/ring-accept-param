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

(deftest match-nil
	(is (= {} (mdw/match-accept nil))))

(deftest match-empty-string
	(is (= {} (mdw/match-accept ""))))

(deftest match-unsupported-format
	(is (= {} (mdw/match-accept "audio/mpeg"))))

(deftest match-json-format
	(is (= {:json "^application/json"} (mdw/match-accept "application/json"))))

(deftest match-html-format
	(is (= {:html "^text/html"} (mdw/match-accept "text/html;level=1"))))

(deftest match-xml-format
	(is (= {:xml "^text/xml"} (mdw/match-accept "text/xml;q=0.8"))))
