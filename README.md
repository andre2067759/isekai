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


## Texture pipeline and new character content

- `tools/generate_textures.py` creates the original ISEKAI pixel-art PNG assets using only Python's standard library. It regenerates the block, item, armor-layer and Aether Sentinel textures before the GitHub Actions Forge build.
- For a local build, run `python tools/generate_textures.py` from the repository root before `gradle build`.
- The art direction uses distinct Aether, Sun and Moon palettes, dark metal silhouettes, bright rune seams and crystalline highlights. These are the first coherent replacement pass; individual assets will continue to receive more detailed custom models and animation.
- `/isekai shop` lists the Aether merchant's offers. Use `/isekai shop buy <item>` to purchase with persistent coins.
- `/isekai quest` now tracks three Sentinel hunt milestones at 3, 8 and 15 kills, each with its own coin and experience reward.


## Expanded relic and material tranche

The build pipeline now produces a second, more detailed pixel-art pass at 32×32 for item and block icons, with distinct Aether, Moon, Sun, Void, Ember and Starlight palettes. It also generates the model JSON and crafting recipes for 15 additional items during the build, so the source remains reproducible from `tools/generate_textures.py`.

New items: Voidsteel, Embersteel and Starlight ingots; three matching crystals; Void Sword, Sun Greatblade, Moon Dagger, Aether Spear and Aether Staff; Void Amulet, Sun Ring, Moon Talisman; Greater Aether Fruit. The new weapons and materials are also purchasable from `/isekai shop`.


### Additional playable magic

The spell command now also includes:
- `/isekai cast void_lance`: a 24-block piercing magic strike, 22 damage, 35 mana, 5-second cooldown.
- `/isekai cast solar_burst`: area fire-magic burst, 16 damage and brief ignition, 45 mana, 7-second cooldown.
- `/isekai cast moon_step`: short collision-checked blink, 20 mana, 5-second cooldown.


## Aether enemy ecosystem

- Aether Sentinel: baseline melee guardian (42 HP, 8 damage).
- Aether Stalker: fast hunter with lower durability (26 HP, 6 damage, 0.36 movement speed), 12 XP and shard/dust drops.
- Aether Brute: rare elite with high durability (92 HP, 14 damage, 12 armor), 45 XP and improved shard/crystal drops with a rare Voidsteel ingot.
- All three variants can spawn naturally in Overworld biomes. For controlled testing use `/summon isekai:aether_stalker` and `/summon isekai:aether_brute`.


## New enemy test tranche

Three additional Aether enemies are registered with dedicated attributes and experience rewards:
- Wraith (38 HP, 9 damage, 55 XP): fast and aggressive.
- Ravager (68 HP, 11 damage, 9 armor, 90 XP): armoured mid-tier fighter.
- Warden (260 HP, 18 damage, 16 armor, 350 XP): boss-scale endurance challenge.

Test directly with:
- `/summon isekai:aether_wraith`
- `/summon isekai:aether_ravager`
- `/summon isekai:aether_warden`

Creative inventory also contains their spawn eggs. Defeating them grants progression XP and contributes to the existing Aether hunt milestones.


## In-game spellcasting and creature eggs

The Aether Staff now provides command-free spell controls:
- Hold the Aether Staff and right-click to cast the selected spell.
- Sneak + right-click to cycle through Aether Bolt, Void Lance, Solar Burst and Moon Step.
- The selected spell is saved in player persistent data; mana costs and cooldowns are still validated server-side.
- The old slash commands remain available as an optional debug/alternative method.

Spawn eggs are available in the ISEKAI creative tab for all six Aether creatures: Sentinel, Stalker, Brute, Wraith, Ravager and Warden.
