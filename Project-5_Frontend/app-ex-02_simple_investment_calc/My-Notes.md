# My Notes

## General Notes
This app is a simple example of the following:

- `useState` to monitor *state* across the app
- "Prop-Drilling" to pass the data down from the *Common Parent* to all applicable children (in lieu of Context)
- User-Input that, as typed, updates in real-time (i.e., no Submit button required)
- Conditional rendering of a component

Based on the break-down provided by the React documentation, it was decided that...

- The `<CalculatorInput>` and `<OutputTable>` components have *state*
- The `<InvestmentCalculator />` component is the *Common Parent*
    - Meaning that it is in charge of handling the state

## Component-Specific
### InvestmentCalculator.tsx
The `handleInputChange` function called a "setter" function that uses, what appears to be, a common `return` design. My code has been updated for a more human-readable version, however this will cover both.

**Explicit Return of Object Literal**
```javascript
setFormData((previousFormData) => {
    return ({
        ...previousFormData,
        [name]: value,
    });
});
```

**Implicit Return of Object Literal**
```javascript
setFormData((previousFormData) => (
	{
        ...previousFormData,
        [name]: value,
    }
));
```

The single-return does not *require* the `return` statement, however I find it confusing to read so I am using the **explicit** version.
