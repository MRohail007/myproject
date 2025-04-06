import { useWindowSize } from 'react-use'
import Confetti from 'react-confetti'

/* This code snippet is a React functional component that uses the `useWindowSize` hook from the
`react-use` library to get the width and height of the window. It then renders the `Confetti`
component from the `react-confetti` library with the width and height obtained from the window size. */
export default () => {
  const { width, height } = useWindowSize()
  return <Confetti width={width} height={height} />
}
