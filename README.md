# ISEKAI

Mod fantasy-RPG per Minecraft Java 1.20.1 / Forge 47.4.28.

## Stato dello sviluppo

Il branch `isekai-full-rework` ospita la ricostruzione progressiva dei sistemi di gioco. Il branch `main` resta la base stabile.

### Contenuti della base

- Materiali e minerali Aether, blocchi e strumenti.
- Equipaggiamento Aether, Solare e Lunare.
- Sentinella dell'Aether con modello, renderer e spawn naturale.
- Ricette, loot table e advancement dedicati.

### Sistemi RPG implementati nel branch di sviluppo

- Progressione persistente dal livello 1 al 100.
- Esperienza per l'uccisione della Sentinella dell'Aether.
- Un punto statistica per ogni livello guadagnato.
- Forza, Vitalità, Agilità, Intelligenza e Spirito.
- Riserva di mana persistente, capacità aumentata da Intelligenza e rigenerazione migliorata da Spirito.
- Primo incantesimo utilizzabile: Dardo di Aether, con costo mana, danno magico, portata e ricarica.
- Identità del personaggio: scelta persistente di razza e classe, con tratti passivi applicati realmente al personaggio.
- Tratti razziali: Umano (+1 fortuna), Elfo (+velocità), Nano (+2 armatura), Demone (+1 danno).
- Tratti di classe: Guerriero (+1,5 danno e +4 salute), Mago (+velocità), Arciere (+velocità e velocità d'attacco).
- Tre abilità attive vincolate alla classe: Fendente del Guerriero, Impulso Arcano del Mago e Raffica dell'Arciere, con mana, bersagli e ricarica.
- Missione iniziale con obiettivo automatico, ricompensa in PE e valuta persistente.
- Portafoglio con monete e prima abilità ad area, Esplosione di Aether.
- Modificatori reali a danno, salute massima e velocità.
- Comandi `/isekai status`, `/isekai mana`, `/isekai allocate <stat> <punti>`, `/isekai cast aether_bolt`, `/isekai profile`, `/isekai quest`, `/isekai wallet` e `/isekai skill`, più `/isekai skill use warrior_cleave`, `/isekai skill use arcane_pulse` e `/isekai skill use ranger_volley`.
- Conservazione dei dati al login e alla rinascita del personaggio.

Le statistiche Intelligenza e Spirito sono già salvate e pronte per essere collegate ai futuri sistemi di mana, magia e coltivazione spirituale.

## Ambiente di compilazione

- Java 17
- Minecraft 1.20.1
- Forge 47.4.28
- Gradle 8.8
