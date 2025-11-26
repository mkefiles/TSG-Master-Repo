# README

## Quick Notes
- The React Compiler is not enabled on this template because of its impact on dev & build performances. To add it, see [this documentation](https://react.dev/learn/react-compiler/installation)
- The versions used by Maximilian are:
  - TypeScript: 4.2.3
  - React (and React DOM): 17.0.2
  - React Scripts: 4.0.3

## React-Specific Notes
The following are miscellaneous notes from the tutorial.

The snippet below, taken from *package.json* is to add the *types* to React, React-DOM and Node:
```json
{
	"@types/node": "^24.6.0",
	"@types/react": "^19.1.16",
	"@types/react-dom": "^19.1.9"
}
```