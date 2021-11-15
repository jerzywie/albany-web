# albany-web

A Clojurescript app that provides various useful functions to Albany coordinators

## Overview

The starting point is the command-line application 'albany-statements'.

The order spreadsheet is uploaded and various outputs can be generated:
- order forms
- statements

## Development

To get an interactive development environment run:

    clojure -M:fig:build

To clean all compiled files:

    rm -rf target/public

To create a production build run:

	rm -rf target/public
	clojure -M:fig:min


## License

Copyright © 2021 Jerzywie

Distributed under the Eclipse Public License version 1.0.
