# Fern Config is a very simple config library

[![fabric](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/fabric_vector.svg)](https://modrinth.com/mod/fern-config/versions?l=fabric)
[![forge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/forge_vector.svg)](https://modrinth.com/mod/fern-config/versions?l=forge)
[![quilt](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/quilt_vector.svg)](https://modrinth.com/mod/fern-config/versions?l=quilt)
[![neoforge](https://raw.githubusercontent.com/Hyperbole-Devs/vectors/8494ec1ac495cfb481dc7e458356325510933eb0/assets/compact/supported/neoforge_vector.svg)](https://modrinth.com/mod/fern-config/versions?l=neoforge)

## Note: Fern Config is still WIP and doesn't include much features

### Feature roadmap:

- [X] JSON5 save format
- [X] TOML save format
- [X] Boolean
- [X] Enum
- [X] Block entity Mask
- [ ] Block Mask
- [ ] Item Mask
- [ ] Entity Mask
- [ ] String
- [X] Integer
- [X] Float
- [X] Double
- [ ] Color
- [ ] Arrays
- [ ] YAML save format
- [ ] JSON save format
- [ ] Separate folder possibility for configs
- [ ] Ingame config editing screen
- [ ] Server-side datadriven config menu on new versions
- [ ] Auto register config-related commands
- [ ] Make a proper wiki

### For developers:

#### Maven:
```groovy
maven {
    name "lemonnik's maven"
    url "http://maven.lemonnik.dev/releases"
}
```

**Implementation in 26.1+**
```groovy
implementation "dev.lemonnik:fern_config:0.1.0+mc26.1.2-fabric"
```

**For older versions:**

#### Fabric loom:
```groovy
modImplementation "dev.lemonnik:fern_config:0.1.0+mc1.21.1-fabric"
```

#### Forge gradle:
```groovy
implementation fg.deof("dev.lemonnik:fern_config:0.1.0+mc1.21.1-forge")
```
