![Knister logo](https://github.com/Zenika/kata-knister/blob/master/doc/logo_black.png?raw=true)


# kata-knister
Kata sur un jeu de dé :-)

Lien du miro : https://miro.com/app/board/o9J_klFJBjg=/
(domaine Zenika.com)

# Back
## Compilation

à la racine du projet docker-compose build

## Run
docker-compose up -d
Possibilité de se brancher en debug en 127.0.0.1:8000

## Arret
docker-compose down

# Front
dans le répertoire front
npm run serve

En fonction de la connexion au back, il faut mettre à jour l'URL dans .env et dans ws.service.ts
