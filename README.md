## Elimination Shuffle

*Eliminační ItemShuffle*

Na začátku jsou všichni hráči teleportováni na spawn minihry. **Spawn s beaconem je třeba postavit**. Poté se jim do chatu napíše item, který mají sehnat. Pokud bude hráč poslední, kdo ještě item nepřinesl nebo pokud uběhne nastavený časový limit *limit možno vypnout nastavením na -1*, hráč vypadává a jde do spectu.

Hráč musí vždy musí přinést item zpátky na spawn a kliknout na beacon.

Pokud se hráč v průběhu hry odpojí je automaticky diskvalifikován a po dalším připojení je přepnut do spectu.

Hra končí ve chvíli, kdy zbyde poslední aktivní hráč.

### Příkazy

    /game init elimination # Vybere minihru a provede prvotní inicializaci minihry
    /game start     # zapne minihru
    /game stop      # ukončí minihru
    /game skip      # přeskočí aktivní item
    /game pause     # pozastaví minihru
    /game resume    # znovu spustí minihru

    /game elimination setspawn # nastaví spawn minihry
    /game elimination setbeacon # nastaví lokaci beaconu ! Nutno se při psaní příkazu dívat na beacon !

### Nastavení (Options) Minihry

Soubor `elimination.json`

    [int] time          # čas jednoho kola, -1 = timer vypnut, 1 - X = délka kola v sekundách. Default: -1
    [int] border        # poloměr hranic světa, 0 = Normální border MC světa. Default: 0
    [bool] giveFood     # Auto give food při startu a po smrti. Default: true
    [bool] giveCompass  # Auto give compass při startu a po smrti. Default: true
    [Location] spawn    # Lokace spawnu minihry. Default: NULL
    [Location] beacon   # Lokace beaconu. Default: NULL
    [bool] peekItem     # Zobrazit ihned další item, po odevzdání starého. Default: false