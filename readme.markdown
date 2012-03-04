Hvordan sette opp EGit
-------------------------------

EGit er en Eclipse-plugin som gjør det lettere for brukere av Eclipse å dele kildekode med andre gjennom versjonskontrollsystemet GIT.

Det første du trenger er en Github konto. Registrer deg på [github.com/signup/free](https://github.com/signup/free) og gi meg brukernavnet ditt så jeg kan invitere deg til prosjektet.

Installasjon av EGit:

* Åpne Eclipse og velg help -> install new software
* Lim http://download.eclipse.org/egit/updates inn i det øverste tekstfeltet
* Utvid begge menyene og velg Eclipse EGit fra den øverste og Eclipse JGit fra den nederste
* Trykk på neste, installer og restart Eclipse

Oppsett av SSH-nøkler:

* Åpne Eclipse og velg Window - Preferences -> General -> Network Connections -> SSH2
* Sjekk at SSHhome-feltet inneholder noe fornuftig. I private keys skal det stå id_dsa,id_rsa
* Velg key management-fanen og trykk Generate RSA Key
* Trykk Save Private Key og overskriv eventuelle filer med samme navn
* Kopier alt i det grå feltet
* Gå inn på [Github](http://www.github.com) kontoen din og velg account settings -> SSH Keys -> Add New SSH Key
* Lim inn public nøkkelen i Key-feltet og trykk Add Key. Title kan godt være blank

Kloning av prosjektet:

* Åpne Eclipse og velg File -> Import -> Git -> Projects from Git -> URI
* Lim inn ssh://git@github.com/LeoWestby/Kalendersystem.git i URI-feltet og trykk neste
* Velg master som branch
* Trykk neste to ganger og velg Import as general project

Nå kan du begynne å jobbe på prosjektet som om det var et helt vanlig prosjekt. Høyreklikk på prosjektet og velg team for å se en liste over funksjonene i EGit. De funksjonene som er viktigst å forstå er pull, commit og push.

Pull:

* For å få tak i den nyeste versjonen av prosjektet, høyreklikk på prosjektet, velg team og trykk pull. Det er veldig viktig at du gjør dette hver gang du skal jobbe med prosjektet for å unngå kollisjoner med noe noen andre har laget.

Commit:

* Hver gang du har laget en ny bit av system eller endret på noe, høyreklikk på prosjektet, velg team og klikk commit. Skriv inn hva du har gjort og sjekk av de filene hvor du har gjort endringer. Det du commiter blir før du pusher bare lagret lokalt på disken.

Push: 

* For å laste opp en ny versjon av systemet til Github som inkluderer dine commiter, høyreklikk på prosjektet, velg team og trykk push. 

Hvor ofte du pusher er opp til deg, men sørg helst for at programmet er kjørbart før du pusher, for etter du har pushet kan det være vanskelig å rulle systemet tilbake til en tidligere versjon.

Hvis du får feilmelding "rejected" når du prøver å pushe, betyr det at du ikke har den nyeste versjonen av systemet. Pull og prøv igjen. Hvis du får en annen feilmelding, betyr det sannsynligvis at dine endringer (din commit) kolliderer med noen andre sin commit. Hvis dette skjer, må man inn i begge filene og manuelt flette dem sammen.

Ønsker du en dypere forståelse for hvordan GIT fungerer, så finnes hele manualen her: [wiki.eclipse.org/EGit/User_Guide](http://wiki.eclipse.org/EGit/User_Guide).