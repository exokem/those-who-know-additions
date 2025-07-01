
# TODO

## Quick Changes

- remove carbon steel paxel

## Electricity

- Machine voltage: 380V
- Machines have a current draw (derive from power rating)
- Wires have a max current - total current is the sum of currents drawn by each machine on the network
- Maybe decrease transfer efficiency based on distance from source to destination - use case for HV DC
- Give network args a key to sort by in request processor
- Give network args what it needs to modify the state of a requester

## High Priority

- repair kits for paxel
- electricity
  1. Create two DeviceStateFunctions - one should pull and one should push
  2. Expose energy through the device design capability interface
  3. Implement INetworkRequestProcessor to distribute energy
- Copper cables should visually connect to electrical devices (use block tags) 
- Investigate intermittent waterlogging (is it a sequence traversal ordering issue? check firstOrNull returns)

## Medium Priority

- Bug: Progress and Progress_Limit must be added to deviceData via .with or progress bars will not render
- Support nonstandard recipe categories in device designs (assembler)
- Consider simplifying fuel + heating logic (no need for complex curves?)

## Low Priority

- Revisit: IAttribute + Necessity
- Design: Electrical tiers system
  - Cable specs (voltage, current) + block tags for connectivity
  - Device specs (voltage, current)
- Remove voltaic references in photon
- Standardize DeviceProperty API - argument order + super callbacks
- Document DeviceProperty API
- Fancy Wires

## Feature Development Ideas

sonic protection for anti-warden activity
see Entity.isInvulnerableTo, Player.abilities, LivingHurtEvent (cancelable)
do something to combat blindness - tech helmet with modules (sonic protection, night vision, etc.)

extruder
 - dies for different cross sections (wire, e.g.)
 - metal is heated first

mechanical wire drawer
 - draw plate (holes for different wire thicknesses)
 - pliers pull wire through
 - only works for very ductile metals (copper, silver, gold, platinum)

missing item replacement (possible?)
tooltips for electrical devices
copper materials
gears?

LATER

device for renaming tools (free) and reordering enchantments

magic protection enchant
modular armor: rocket boots - hold space to go up (like jetpack)
modular armor: thruster module (for chestplate?) - allows boosted lateral movement when in the air

computers with programs + custom languages
networking + fancy bundled cables
IO interfaces
modeling software + 3D models for voxel sub-block creation
milling machines for metal blocks - require models on cartridges
laser cutter machine for 2D parts

"download" schematics from the aethernet
"download" illegal schematics from the nethernet

cybernetics
potion injectors (can inject more to increase intensity at a cost/risk - todo decide drawback)
elixirs (different potion system)
alternative enchantment system
modular tools

binder
typewriter
printer
documentation pages for mod structures/features - put them in a binder
write your own things - put them in a binder
shelves for binders
labels for binders

soviet themed blocks
signs with larger/bold custom fonts
armor skins

solar furnace
- charging phase to charge internal battery
- when fully charged, it starts smelting until the battery is depleted

big chemical reactors
computer systems to perform complex synthesis
elemental storage
enumerate elements instead of metals

warden heart (item)
warden heart extractor (tool, instant kills warden + gives heart)

mob capture device

metal gear sound when spotted by mobs
mob detector/radar hud

cables

very large nanite (entity) like a bee
nanites for tool repair (material is still required)

charms for different bonuses
rename copper, iron, gold to relic variants

disable spawning from nether portals

https://en.wikipedia.org/wiki/Mineral-insulated_copper-clad_cable