import faker from "faker";
import { sample } from "lodash";
// utils

// ----------------------------------------------------------------------

const volunteers = [...Array(24)].map((_, index) => ({
  id: faker.datatype.uuid(),
  avatarUrl: "https://avatars.dicebear.com/api/bottts/ali%20ahnaf.svg",
  name: faker.name.findName(),
  email: faker.company.companyName() + "@" + faker.internet.domainName(),
  semester: (faker.datatype.number() % 6) + 1 + ".1",
  dept: sample(["CSE", "MPE", "IPE", "EEE"]),
  roll: faker.datatype.uuid(),
  isPending: sample(["Active", "Pending"]),
}));

export default volunteers;
