# ISEKAI

Mod fantasy-RPG per Minecraft Java 1.20.1 / Forge 47.4.28.

## Stato dello sviluppo

Il branch `isekai-full-rework` ospita la ricostruzione progressiva dei sistemi di gioco. Il branch `main` resta la base stabile.

### Sistemi già presenti nella base

- Materiali e minerali Aether, blocchi e strumenti.
- Equipaggiamento Aether, Solare e Lunare.
- Sentinella dell'Aether con modello, renderer e spawn naturale.
- Ricette, loot table e advancement dedicati.

### Prima fase RPG: progressione del personaggio

- Livello iniziale: 1.
- Livello massimo attuale: 100.
- Esperienza persistente nei dati del giocatore.
- Ricompensa iniziale: 45 PE per sconfiggere una Sentinella dell'Aether.
- Curva iniziale: 100 PE per passare dal livello 1 al 2, con incremento di 50 PE per ogni livello successivo.
- Comando `/isekai status` per consultare livello ed esperienza.
- Dati copiati quando Minecraft ricrea l'entità giocatore.

Questa è la prima infrastruttura del sistema RPG: abilità, attributi, classi, razze, UI e ricompense aggiuntive verranno costruiti sopra questa base, senza introdurre dipendenze obbligatorie da altre mod.

## Ambiente di compilazione

- Java 17
- Minecraft 1.20.1
- Forge 47.4.28
- Gradle Wrapper del progetto
