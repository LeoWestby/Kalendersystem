Hvordan sette opp EGit
-------------------------------

EGit er en Eclipse-plugin som gj�r det lettere for brukere av Eclipse � dele kildekode med andre gjennom versjonskontrollsystemet GIT.

Det f�rste du trenger er en Github konto. Registrer deg p� [github.com/signup/free](https://github.com/signup/free) og gi meg brukernavnet ditt s� jeg kan invitere deg til prosjektet.

Installasjon av EGit:

* �pne Eclipse og velg help -> install new software
* Lim http://download.eclipse.org/egit/updates inn i det �verste tekstfeltet
* Utvid begge menyene og velg Eclipse EGit fra den �verste og Eclipse JGit fra den nederste
* Trykk p� neste, installer og restart Eclipse

Oppsett av SSH-n�kler:

* �pne Eclipse og velg Window - Preferences -> General -> Network Connections -> SSH2
* Sjekk at SSHhome-feltet inneholder noe fornuftig. I private keys skal det st� id_dsa,id_rsa
* Velg key management-fanen og trykk Generate RSA Key
* Trykk Save Private Key og overskriv eventuelle filer med samme navn
* Kopier alt i det gr� feltet
* G� inn p� [Github](http://www.github.com) kontoen din og velg account settings -> SSH Keys -> Add New SSH Key
* Lim inn public n�kkelen i Key-feltet og trykk Add Key. Title kan godt v�re blank

Kloning av prosjektet:

* �pne Eclipse og velg File -> Import -> Git -> Projects from Git -> URI
* Lim inn ssh://git@github.com/LeoWestby/Kalendersystem.git i URI-feltet og trykk neste
* Velg master som branch
* Trykk neste to ganger og velg Import as general project

N� kan du begynne � jobbe p� prosjektet som om det var et helt vanlig prosjekt. H�yreklikk p� prosjektet og velg team for � se en liste over funksjonene i EGit. De funksjonene som er viktigst � forst� er pull, commit og push.

Pull:

* For � f� tak i den nyeste versjonen av prosjektet, h�yreklikk p� prosjektet, velg team og trykk pull. Det er veldig viktig at du gj�r dette hver gang du skal jobbe med prosjektet for � unng� kollisjoner med noe noen andre har laget.

Commit:

* Hver gang du har laget en ny bit av system eller endret p� noe, h�yreklikk p� prosjektet, velg team og klikk commit. Skriv inn hva du har gjort og sjekk av de filene hvor du har gjort endringer. Det du commiter blir f�r du pusher bare lagret lokalt p� disken.

Push: 

* For � laste opp en ny versjon av systemet til Github som inkluderer dine commiter, h�yreklikk p� prosjektet, velg team og trykk push. 

Hvor ofte du pusher er opp til deg, men s�rg helst for at programmet er kj�rbart f�r du pusher, for etter du har pushet kan det v�re vanskelig � rulle systemet tilbake til en tidligere versjon.

Hvis du f�r feilmelding "rejected" n�r du pr�ver � pushe, betyr det at du ikke har den nyeste versjonen av systemet. Pull og pr�v igjen. Hvis du f�r en annen feilmelding, betyr det sannsynligvis at dine endringer (din commit) kolliderer med noen andre sin commit. Hvis dette skjer, m� man inn i begge filene og manuelt flette dem sammen.

�nsker du en dypere forst�else for hvordan GIT fungerer, s� finnes hele manualen her: [wiki.eclipse.org/EGit/User_Guide](http://wiki.eclipse.org/EGit/User_Guide).