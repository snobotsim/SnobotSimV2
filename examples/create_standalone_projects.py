import shutil
import distutils.dir_util
import argparse
import os


class ExampleConfig:
    def __init__(
        self, package_name, output_project, vendor_props, base_project="test_robot_base"
    ):
        self.package_name = package_name
        self.output_project = output_project
        self.vendor_props = vendor_props
        self.base_project = base_project


def run():

    parser = argparse.ArgumentParser()
    parser.add_argument("--output_directory", type=str, required=True)

    args = parser.parse_args()

    default_vendor_props = ["SnobotSim.json", "WPILibNewCommands.json"]

    projects = []
    projects.append(ExampleConfig("rev", "Rev", ["navx_frc.json", "REVLib.json"]))
    projects.append(ExampleConfig("ctre", "Ctre", ["Phoenix.json"]))
    projects.append(ExampleConfig("wpi", "Wpi", []))
    projects.append(
        ExampleConfig(
            "catchall", "Catchall", ["navx_frc.json", "REVLib.json", "Phoenix.json"]
        )
    )
    projects.append(
        ExampleConfig(
            "ctre_swerve", "CtreSwerve", ["Phoenix.json"], "test_robot_base_swerve"
        )
    )
    projects.append(
        ExampleConfig(
            "rev_swerve",
            "RevSwerve",
            ["REVLib.json", "Phoenix.json"],
            "test_robot_base_swerve",
        )
    )

    this_dir = os.path.dirname(os.path.abspath(__file__))

    for config in projects:
        base_project = os.path.join(this_dir, config.base_project)
        local_project = os.path.join(this_dir, f"test_robot_{config.package_name}")
        output_project = os.path.join(
            args.output_directory, f"RobotForBasicSimTesting{config.output_project}"
        )

        print("Copying")
        print(f"  {base_project}\n  {local_project}\n  to\n  {output_project}")

        # Copy the standard WPILib / Gradle files
        distutils.dir_util.copy_tree(
            this_dir + "/project_files/standard", output_project
        )

        # Generate the build file
        with open(
            os.path.join(this_dir, "project_files", "build.gradle.template"), "r"
        ) as template_file:
            template_contents = template_file.read()

            with open(os.path.join(output_project, "build.gradle"), "w") as of:
                of.write(template_contents.replace("$(SIMTYPE)", config.package_name))

        # Copy the source from this project and the base
        distutils.dir_util.copy_tree(local_project + "/src", output_project + "/src")
        distutils.dir_util.copy_tree(base_project + "/src", output_project + "/src")

        # Copy the vendor deps for this project
        dest_vendordeps = os.path.join(output_project, "vendordeps")
        if os.path.exists(dest_vendordeps):
            shutil.rmtree(dest_vendordeps)
        os.mkdir(dest_vendordeps)

        all_vendor_props = config.vendor_props + default_vendor_props
        for vp in all_vendor_props:
            shutil.copy(
                os.path.join(this_dir, "project_files", "vendor_props", vp),
                os.path.join(output_project, "vendordeps", vp),
            )


if __name__ == "__main__":
    run()
