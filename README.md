# Fern Config
[![fabric](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/fabric_vector.svg)](https://modrinth.com/mod/fern-config/versions?l=fabric)
[![forge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/forge_vector.svg)](https://modrinth.com/mod/fern-config/versions?l=forge)\
[![quilt](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/quilt_vector.svg)](https://modrinth.com/mod/fern-config/versions?l=quilt)
[![neoforge](https://raw.githubusercontent.com/Hyperbole-Devs/vectors/8494ec1ac495cfb481dc7e458356325510933eb0/assets/compact/supported/neoforge_vector.svg)](https://modrinth.com/mod/fern-config/versions?l=neoforge)
---
Fern config is a light and simple config library that allows developers to easily create configs and store them in different formats

Supported formats:
- JSON5
- TOML


Things that are done in the latest version:

- JSON5 save format
- TOML save format
- Boolean
- Enum
- Float
- Double
- Integer
- String
- Float arrays
- Color utils
- Mask

## What's "Mask"?
Mask is basically a selector for blocks/items/entities, that can work as a **whitelist** or **blacklist**\

<details>
<summary>For developers</summary>
Maven:

```groovy
maven {
    name "lemonnik's maven"
    url "https://maven.lemonnik.dev/releases"
}
```


**Implementation in 26.1+**
```groovy
implementation "dev.lemonnik:fern_config:0.1.0+mc26.1.2-fabric"
```

**For older versions:**

Fabric loom:
```groovy
modImplementation "dev.lemonnik:fern_config:0.1.0+mc1.21.1-fabric"
```

Forge gradle:
```groovy
implementation fg.deof("dev.lemonnik:fern_config:0.1.0+mc1.21.1-forge")
```
Available versions can be found on [maven](https://maven.lemonnik.dev/#/releases/dev/lemonnik/fern_config)

Other examples are available in gallery, proper wiki will be crated later
</details>
