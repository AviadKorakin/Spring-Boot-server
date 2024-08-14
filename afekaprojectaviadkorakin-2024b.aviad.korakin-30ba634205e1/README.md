<p class="has-line-data" data-line-start="1" data-line-end="2"><strong>How to run the server:</strong></p>
<p class="has-line-data" data-line-start="3" data-line-end="4">This guide assumes you already have the application downloaded and the necessary development environment set up.</p>
<p class="has-line-data" data-line-start="5" data-line-end="6"><strong>Prerequisites:</strong></p>
<ul>
<li class="has-line-data" data-line-start="7" data-line-end="9">Java Development Kit (JDK) 21 or later</li>
</ul>
<p class="has-line-data" data-line-start="9" data-line-end="10"><strong>Instructions:</strong></p>
<ol>
<li class="has-line-data" data-line-start="11" data-line-end="14">
<p class="has-line-data" data-line-start="11" data-line-end="12"><strong>Configure Gradle Nature:</strong></p>
<ul>
<li class="has-line-data" data-line-start="12" data-line-end="14">In your IDE, go to <strong>Configure</strong> and then select <strong>Add Gradle Nature</strong>. This will configure your project to use Gradle for building.</li>
</ul>
</li>
<li class="has-line-data" data-line-start="14" data-line-end="17">
<p class="has-line-data" data-line-start="14" data-line-end="15"><strong>Set Up Build Path:</strong></p>
<ul>
<li class="has-line-data" data-line-start="15" data-line-end="17">Go to <strong>Build Path</strong> and then select <strong>Configure Build Path</strong>.</li>
</ul>
</li>
<li class="has-line-data" data-line-start="17" data-line-end="23">
<p class="has-line-data" data-line-start="17" data-line-end="18"><strong>Add JRE:</strong></p>
<ul>
<li class="has-line-data" data-line-start="18" data-line-end="19">In the Build Path configuration window, expand the <strong>Libraries</strong> section.</li>
<li class="has-line-data" data-line-start="19" data-line-end="20">Click on <strong>Java SE-11</strong> (or similar) and then select <strong>EDIT</strong>.</li>
<li class="has-line-data" data-line-start="20" data-line-end="21">Choose <strong>Installed JREs</strong>.</li>
<li class="has-line-data" data-line-start="21" data-line-end="23">Click <strong>Add…</strong>.</li>
</ul>
</li>
<li class="has-line-data" data-line-start="23" data-line-end="27">
<p class="has-line-data" data-line-start="23" data-line-end="24"><strong>Select JDK Location:</strong></p>
<ul>
<li class="has-line-data" data-line-start="24" data-line-end="25">Navigate to the folder containing your JDK installation (e.g., <code>C:\Integrativite\javaenv2024b\jdk-21.0.3+9</code>).</li>
<li class="has-line-data" data-line-start="25" data-line-end="27">Select the appropriate JDK folder and click <strong>Open</strong>.</li>
</ul>
</li>
<li class="has-line-data" data-line-start="27" data-line-end="31">
<p class="has-line-data" data-line-start="27" data-line-end="28"><strong>Confirm JRE Selection:</strong></p>
<ul>
<li class="has-line-data" data-line-start="28" data-line-end="29">In the Edit JRE window, ensure the newly added JDK is selected.</li>
<li class="has-line-data" data-line-start="29" data-line-end="31">Click <strong>OK</strong> to confirm.</li>
</ul>
</li>
<li class="has-line-data" data-line-start="31" data-line-end="35">
<p class="has-line-data" data-line-start="31" data-line-end="32"><strong>Run the Server:</strong></p>
<ul>
<li class="has-line-data" data-line-start="32" data-line-end="33">In your IDE, go to <strong>Run</strong> and then select your project name.</li>
<li class="has-line-data" data-line-start="33" data-line-end="35">Choose <strong>Start</strong> to run the server.</li>
</ul>
</li>
</ol>
<p class="has-line-data" data-line-start="35" data-line-end="36"><strong>Accessing the Application:</strong></p>
<ul>
<li class="has-line-data" data-line-start="37" data-line-end="38"><strong>API Documentation:</strong> <a href="http://localhost:8084/swagger-ui/index.html#/">http://localhost:8084/swagger-ui/index.html#/</a></li>
<li class="has-line-data" data-line-start="38" data-line-end="40"><strong>H2 Database Console:</strong> <a href="http://localhost:8084/h2-console">http://localhost:8084/h2-console</a></li>
</ul>
<p class="has-line-data" data-line-start="40" data-line-end="41"><strong>Viewing the Database:</strong></p>
<p class="has-line-data" data-line-start="42" data-line-end="43"><strong>Using Bash (Container):</strong></p>
<ol>
<li class="has-line-data" data-line-start="44" data-line-end="46">
<p class="has-line-data" data-line-start="44" data-line-end="45"><strong>Open Terminal:</strong> Launch a terminal window.</p>
</li>
<li class="has-line-data" data-line-start="46" data-line-end="51">
<p class="has-line-data" data-line-start="46" data-line-end="47"><strong>Get Container ID:</strong> Within your terminal, identify the container ID for the running application</p>
<pre><code class="has-line-data" data-line-start="48" data-line-end="50" class="language-bash">docker os
</code></pre>
</li>
<li class="has-line-data" data-line-start="51" data-line-end="57">
<p class="has-line-data" data-line-start="51" data-line-end="52"><strong>Enter Container:</strong> Use the following command, replacing <code>(Container Id)</code> with the copied ID:</p>
<pre><code class="has-line-data" data-line-start="54" data-line-end="56" class="language-bash">docker <span class="hljs-built_in">exec</span> -it (Container Id) bash
</code></pre>
</li>
<li class="has-line-data" data-line-start="57" data-line-end="63">
<p class="has-line-data" data-line-start="57" data-line-end="58"><strong>Connect to Database:</strong> Execute the following command, replacing <code>POSTGRES_USER</code> and <code>POSTGRES_DB</code> with your actual credentials from <code>compose.yaml</code>:</p>
<pre><code class="has-line-data" data-line-start="60" data-line-end="62" class="language-bash">psql -U POSTGRES_USER -W POSTGRES_DB
</code></pre>
</li>
<li class="has-line-data" data-line-start="63" data-line-end="65">
<p class="has-line-data" data-line-start="63" data-line-end="64"><strong>Enter Password:</strong> When prompted, enter the database password (<code>POSTGRES_PASSWORD</code>).</p>
</li>
<li class="has-line-data" data-line-start="65" data-line-end="67">
<p class="has-line-data" data-line-start="65" data-line-end="66"><strong>List Tables:</strong> Use the command <code>\d</code> to view the database tables.</p>
</li>
</ol>
<p class="has-line-data" data-line-start="67" data-line-end="68"><strong>Using H2 Console:</strong></p>
<ol>
<li class="has-line-data" data-line-start="69" data-line-end="71">
<p class="has-line-data" data-line-start="69" data-line-end="70"><strong>Open H2 Console:</strong> Go to <a href="http://localhost:8084/h2-console">http://localhost:8084/h2-console</a> in your web browser.</p>
</li>
<li class="has-line-data" data-line-start="71" data-line-end="73">
<p class="has-line-data" data-line-start="71" data-line-end="72"><strong>Select Settings:</strong> Choose “Generic PostgreSQL” as the setting name.</p>
</li>
<li class="has-line-data" data-line-start="73" data-line-end="75">
<p class="has-line-data" data-line-start="73" data-line-end="74"><strong>Find JDBC URL:</strong> Locate the line that mentions “H2 console available at ‘/h2-console’. Database available at ‘jdbc:postgresql://127.0.0.1:XXXX/mydatabase’”.</p>
</li>
<li class="has-line-data" data-line-start="75" data-line-end="77">
<p class="has-line-data" data-line-start="75" data-line-end="76"><strong>Paste URL:</strong> Copy the provided JDBC URL (e.g., <code>jdbc:postgresql://127.0.0.1:XXXX/mydatabase</code>). Paste this URL into the JDBC URL field on the H2 Console page.</p>
</li>
<li class="has-line-data" data-line-start="77" data-line-end="79">
<p class="has-line-data" data-line-start="77" data-line-end="78"><strong>Enter Credentials:</strong> Enter the username (<code>POSTGRES_USER</code>) and password (<code>POSTGRES_PASSWORD</code>) from your <code>compose.yaml</code> file.</p>
</li>
<li class="has-line-data" data-line-start="79" data-line-end="81">
<p class="has-line-data" data-line-start="79" data-line-end="80"><strong>Connect:</strong> Click “Connect” to access the database tables.</p>
</li>
</ol>
<p class="has-line-data" data-line-start="81" data-line-end="82"><strong>Testing Sphere Radius:</strong></p>
<p class="has-line-data" data-line-start="83" data-line-end="84">This section describes how to manually test a sphere radius using a separate tool, not within the application itself.</p>
<ol>
<li class="has-line-data" data-line-start="85" data-line-end="87">
<p class="has-line-data" data-line-start="85" data-line-end="86"><strong>Input Location and Radius:</strong> Define your current location (latitude and longitude) and the desired sphere radius.</p>
</li>
<li class="has-line-data" data-line-start="87" data-line-end="89">
<p class="has-line-data" data-line-start="87" data-line-end="88"><strong>Calculate Distance:</strong> Use a tool like <a href="https://www.calculator.net/distance-calculator.html">https://www.calculator.net/distance-calculator.html</a> to calculate the distance between two points on a sphere.</p>
</li>
<li class="has-line-data" data-line-start="89" data-line-end="91">
<p class="has-line-data" data-line-start="89" data-line-end="90"><strong>Compare Results:</strong> Enter two points within your specified radius and calculate the distance between them. Compare this distance with the expected results from your sphere radius test.</p>
</li>
</ol>
<p class="has-line-data" data-line-start="91" data-line-end="92"><strong>Note:</strong> There may be slight discrepancies between the calculated distance and the actual sphere radius due to rounding within functions.</p>
<p class="has-line-data" data-line-start="91" data-line-end="93"><strong>Obtaining the Code:</strong><br>
<a href="https://bitbucket.org/afekaprojectaviadkorakin/2024b.aviad.korakin/src/master/">https://bitbucket.org/afekaprojectaviadkorakin/2024b.aviad.korakin/src/master/</a></p>