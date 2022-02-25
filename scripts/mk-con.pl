

open(FD, $ARGV[0]) || die "$!";

while( <FD> )
{
  m/.*<td>.*<td>.*<td>.*<td>.*<td>(.*[^<])/;
  print "$1";
}