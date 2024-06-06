package processor

import io.apibuilder.task.v0.models.TaskType

import javax.inject.Inject

class TaskActorCompanion @Inject() (
  indexApplication: IndexApplicationProcessor,
  diffVersion: DiffVersionProcessor,
  cleanupDeletions: CleanupDeletionsProcessor,
  scheduleMigrateVersions: ScheduleMigrateVersionsProcessor,
  migrateVersion: MigrateVersionProcessor
) {

  def process(typ: TaskType): Unit = {
    lookup(typ).process()
  }

  private[this] def lookup(typ: TaskType): BaseTaskProcessor = {
    import TaskType._
    typ match {
      case IndexApplication => indexApplication
      case CleanupDeletions => cleanupDeletions
      case DiffVersion => diffVersion
      case MigrateVersion => migrateVersion
      case ScheduleMigrateVersions => scheduleMigrateVersions
      case UNDEFINED(_) => sys.error(s"Undefined task type '$typ")
    }
  }
}
