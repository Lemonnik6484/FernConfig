# Fern Config is a very simple config library

## Note: Fern Config is still WIP and doesn't include much features

### Feature roadmap:

- [X] JSON5 save format
- [X] TOML save format
- [X] Boolean
- [X] Enum
- [X] Block Mask
- [ ] Item Mask
- [ ] Entity Mask
- [ ] String
- [ ] Integer
- [ ] Float
- [ ] Color
- [ ] Double
- [ ] Arrays
- [ ] YAML save format
- [ ] JSON save format
- [ ] Separate folder possibility for configs
- [ ] Ingame config editing screen
- [ ] Server-side datadriven config menu on new versions
- [ ] Auto register config-related commands
- [ ] Make a good wiki

### For developers:

#### Maven:
```groovy
maven {
    name "lemonnik's maven"
    url "http://lemonnik.ddns.net:16555/releases"
    allowInsecureProtocol = true
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
