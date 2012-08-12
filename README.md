# Hangman

Hulu interview challenge, circa S09. An intelligent hangman player.

# Client

client/

## Build

    ant build

## Usage

    java -jar dist/Hangman-0.1.jar

# Server

server/

## Usage

    node server.js

# Example

    __ ______ _ ______ __ ____-______
    a, true
    __ ______ a ______ __ _a__-______
    o, true
    o_ ______ a _o____ o_ _a__-______
    f, true
    o_ ______ a _o____ of _a__-______
    n, true
    o_ ______ a _o____ of _a__-_n____
    r, true
    o_ __r___ a _o____ of _a__-_n____
    h, true
    oh __r___ a _o____ of _a__-_n____
    e, true
    oh _ere__ a _o___e of _a_e-_n__e_
    v, true
    oh _ere__ a _o___e of _a_e-_n_ve_
    m, true
    oh mere__ a _o___e of _a_e-_n_ve_
    l, true
    oh merel_ a _o__le of _a_e-_n_ve_
    y, true
    oh merely a _o__le of _a_e-_n_ve_
    c, true
    oh merely a co__le of ca_e-_n_ve_
    s, true
    oh merely a co__le of case-_n_ves
    p, true
    oh merely a co_ple of case-_n_ves
    u, true
    oh merely a couple of case-_n_ves
    k, true
    oh merely a couple of case-kn_ves
    i, true
    oh merely a couple of case-knives
    Success with 0 error(s).
