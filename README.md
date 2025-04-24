# 🧠 AllayaSkills
Um sistema completo de habilidades passivas evolutivas, feito para servidores PvP e PvE no Minecraft 1.21.4.

## ✨ Funcionalidades
- 🌿 Árvore de habilidades passivas desbloqueáveis com pontos
- 🛣️ Caminhos evolutivos configuráveis (ex: combate, mineração)
- ⛏️ XP baseado em ações (matar, minerar, etc)
- 🔐 Limitações por região (WorldGuard), mundo, ferramenta, permissão, chance
- 🧠 Sistema de pontos por nível
- 📀 Armazenamento local com SQLite
- 🔹 Placeholders integrados (PlaceholderAPI)
- 📆 GUI customizável estilo CommandPanels, via YAML
- 📆 BossBar de progresso configurável e integrada por path

## 📁 Requisitos
- Minecraft 1.21.4
- Java 21
- Recomendado: WorldGuard, PlaceholderAPI

## 📂 Exemplos de Arquivos
### `paths/mineracao.yml`
```yaml
display-name: "Caminho da Mineração"
xp-source: BLOCK_BREAK
xp-per-event: 10
starting-xp: 100
xp-increase-per-level: 50
points-per-level: 1
block-xp:
  DIAMOND_ORE: 30
  IRON_ORE: 10
  STONE: 1
```

### `skills/maos_rapidas.yml`
```yaml
name: "Mãos Rápidas"
description: "+10% velocidade"
path: "mineracao"
unlock_level: 0
skill-points-required: 1
requires: []
region-allowed: []
cooldown: 0
effects:
  - trigger: PASSIVE_TICK
    effect: POTION_EFFECT
    type: FAST_DIGGING
    amplifier: 0
    duration: 40
```

### `menus/main.yml`
```yaml
title: "Menu Principal"
rows: 3

items:
  skills:
    slot: 11
    material: ENCHANTED_BOOK
    name: "&bÁrvore de Habilidades"
    lore:
      - "&7Visualize e desbloqueie skills!"
    open-menu: "mine-skills"

  stats:
    slot: 15
    material: PAPER
    name: "&aEstatísticas"
    lore:
      - "&7Veja seu progresso e nível"
    open-menu: "stats"
```

### `menus/mine-skill.yml`
```yaml
title: "Árvore: Mineração"
rows: 4

items:
  maos_rapidas:
    slot: 10
    type: "skill"
    locked:
      material: RED_STAINED_GLASS_PANE
      name: "&aMãos Rápidas"
      lore:
        - "&7+10% velocidade de mineração"
        - "&aCusto: 1 Ponto de habilidade."
        - "&aSeus pontos: %points_mineracao%"
      unlock-skill: "maos_rapidas"
    unlocked:
      material: LIME_STAINED_GLASS_PANE
      name: "&aMãos Rápidas"
      lore:
        - "&7+10% velocidade de mineração"
  super_toque:
    slot: 13
    type: "skill"
    locked:
      material: RED_STAINED_GLASS_PANE
      name: "&bSuper Toque"
      lore:
        - "&7Minérios dropam automaticamente"
        - "&aCusto: 1 Ponto de habilidade."
        - "&aSeus pontos: %points_mineracao%"
      unlock-skill: "super_toque"
    unlocked:
      material: LIME_STAINED_GLASS_PANE
      name: "&bSuper Toque"
      lore:
        - "&7Minérios dropam automaticamente"
  voltar:
    slot: 31
    type: "static"
    material: BARRIER
    name: "&cVoltar"
    lore:
      - "&7Retornar ao menu principal"
    back: true
```

## 📅 Comandos
```
/skills                         - Abre o menu principal
/skills open <path>            - Abre uma árvore diretamente
/skills invest <skill-id>      - Desbloqueia skill
/skills points                 - Mostra pontos disponíveis
/skills info <jogador>         - Progresso de outro jogador
/skills givexp <jogador> <caminho> <qtd>   - XP admin
/skills givepoints <jogador> <caminho> <qtd> - Pontos admin
/skills reset <jogador> <path|all>         - Resetar progresso
```

## 🔐 Permissões
- `skilltree.use` - Acessar menus e sistema
- `skilltree.admin` - Comandos administrativos
- `skilltree.reset.self` - Resetar próprio progresso
- `skilltree.view.self` - Ver progresso pessoal

## 🔹 Placeholders (PAPI)
- `%allayaskills_level_<path>%`
- `%allayaskills_xp_<path>%`
- `%allayaskills_nextxp_<path>%`
- `%allayaskills_points_<path>%`
- `%allayaskills_has_skill_<id>%`

## 📃 Banco de Dados
```sql
CREATE TABLE player_skills (
  uuid TEXT NOT NULL,
  path TEXT NOT NULL,
  level INTEGER,
  current_xp INTEGER,
  xp_needed INTEGER,
  points_earned INTEGER,
  points_used INTEGER,
  PRIMARY KEY (uuid, path)
);

CREATE TABLE player_unlocked_skills (
  uuid TEXT NOT NULL,
  skill_id TEXT NOT NULL,
  path TEXT NOT NULL,
  PRIMARY KEY (uuid, skill_id)
);
```

## 📆 Estrutura
```
AllayaSkills/
├── config.yml
├── messages.yml
├── skills/
│   ├── maos_rapidas.yml
│   ├── super_toque.yml
├── paths/
│   └── mineracao.yml
├── menus/
│   ├── main.yml
│   ├── mine-skill.yml
│   └── stats.yml
├── database.db
```

## ✅ Futuro
- Especializações: Gladiador, Arqueiro, Guardião
- Skills ativas com cooldown
- Ranking: `/skills top <path>`
- Integração com MySQL
- Recompensas por nível (titles, sons, partículas)
- Eventos de XP dobrado por tempo

## 🤝 Contribua
Pull requests são bem-vindos! Melhore funcionalidades, envie novas skills ou integrações.

## 📜 Licença
MIT License.

