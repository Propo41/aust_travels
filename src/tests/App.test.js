import { render, screen } from '@testing-library/react';
import App from '../App';

//https://testing-library.com/docs/react-testing-library/example-intro
test('renders learn react link', () => {
  render(<App />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});
